package com.dataflow.frame.core.loader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.jar.JarFile;

/**
 * 自定义类加载器
 * 加载class的机制违反了传统的双亲委托机制, 默认先从本地加载, 加载不到的情况下才委托父级ClassLoader加载。
 * 原因是为了实现不同插件之间依赖库的隔离。
 */
public class DataFlowLoader extends URLClassLoader {

    private String name;

    public DataFlowLoader(URL[] url, ClassLoader classLoader ) {
        super( url, classLoader );
    }

    public DataFlowLoader(URL[] url, ClassLoader classLoader, String name ) {
        this( url, classLoader );
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected Class<?> loadClassFromThisLoader( String arg0, boolean arg1 ) throws ClassNotFoundException {
        Class<?> clz = null;
        if ( ( clz = findLoadedClass( arg0 ) ) != null ) {
            if ( arg1 ) {
                resolveClass( clz );
            }
            return clz;
        }

        if ( ( clz = findClass( arg0 ) ) != null ) {
            if ( arg1 ) {
                resolveClass( clz );
            }
            return clz;
        }
        return clz;
    }

    protected Class<?> loadClassFromParent( String arg0, boolean arg1 ) throws ClassNotFoundException {
        Class<?> clz;
        if ( ( clz = getParent().loadClass( arg0 ) ) != null ) {
            if ( arg1 ) {
                resolveClass( clz );
            }
            return clz;
        }
        throw new ClassNotFoundException( "Could not find :" + arg0 );
    }

    @Override
    protected synchronized Class<?> loadClass( String arg0, boolean arg1 ) throws ClassNotFoundException {
        try {
            return loadClassFromThisLoader( arg0, arg1 );
        } catch ( ClassNotFoundException | NoClassDefFoundError e ) {
            // ignore
        }
        return loadClassFromParent( arg0, arg1 );
    }

    /*
     * Cglib doe's not creates custom class loader (to access package methotds and classes ) it uses reflection to invoke
     * "defineClass", but you can call protected method in subclass without problems:
     */
    public Class<?> loadClass( String name, ProtectionDomain protectionDomain ) {
        Class<?> loaded = findLoadedClass( name );
        if ( loaded == null ) {
            // Get the jar, load the bytes from the jar file, construct class from scratch as in snippet below...

            /*
             *
             * loaded = super.findClass(name);
             *
             * URL url = super.findResource(newName);
             *
             * InputStream clis = getResourceAsStream(newName);
             */

            String newName = name.replace( '.', '/' );
            InputStream is = super.getResourceAsStream( newName );
            byte[] driverBytes = toBytes( is );
            assert driverBytes != null;
            loaded = super.defineClass( name, driverBytes, 0, driverBytes.length, protectionDomain );

        }
        return loaded;
    }

    private byte[] toBytes( InputStream is ) {
        byte[] retval = new byte[0];
        try {
            int a = is.available();
            while ( a > 0 ) {
                byte[] buffer = new byte[a];
                is.read( buffer );

                byte[] newretval = new byte[retval.length + a];

                // old part
                System.arraycopy(retval, 0, newretval, 0, retval.length);
                // new part
                System.arraycopy(buffer, 0, newretval, retval.length, a);

                retval = newretval;

                a = is.available(); // see what's left
            }
            return retval;
        } catch ( Exception e ) {
            System.out.println("UnableToReadClass" + e.toString());
            return null;
        }
    }

    private static Object getFieldObject( Class<?> clazz, String name, Object obj ) throws Exception {
        Field field = clazz.getDeclaredField( name );
        field.setAccessible( true );
        return field.get( obj );
    }

    /**
     * This method is designed to clear out classloader file locks in windows.
     *
     */
    public void closeClassLoader() {
        HashSet<String> closedFiles = new HashSet<>();
        try {
            Object obj = getFieldObject( URLClassLoader.class, "ucp", this );
            ArrayList<?> loaders = (ArrayList<?>) getFieldObject( obj.getClass(), "loaders", obj );
            for ( Object ldr : loaders ) {
                try {
                    JarFile file = (JarFile) getFieldObject( ldr.getClass(), "jar", ldr );
                    closedFiles.add( file.getName() );
                    file.close();
                } catch ( Exception e ) {
                    // skip
                }
            }
        } catch ( Exception e ) {
            // skip
        }

        try {
            Vector<?> nativeLibArr = (Vector<?>) getFieldObject( ClassLoader.class, "nativeLibraries", this );
            for ( Object lib : nativeLibArr ) {
                try {
                    Method fMethod = lib.getClass().getDeclaredMethod( "finalize");
                    fMethod.setAccessible( true );
                    fMethod.invoke(lib);
                } catch ( Exception e ) {
                    // skip
                }
            }
        } catch ( Exception e ) {
            // skip
        }

        HashMap<?, ?> uCache = null;
        HashMap<?, ?> fCache = null;

        try {
            Class<?> jarUrlConnClass = null;
            try {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                jarUrlConnClass = contextClassLoader.loadClass( "sun.net.www.protocol.jar.JarURLConnection" );
            } catch ( Throwable skip ) {
                // skip
            }
            if ( jarUrlConnClass == null ) {
                jarUrlConnClass = Class.forName( "sun.net.www.protocol.jar.JarURLConnection" );
            }
            Class<?> factory = getFieldObject( jarUrlConnClass, "factory", null ).getClass();
            try {
                fCache = (HashMap<?, ?>) getFieldObject( factory, "fileCache", null );
            } catch ( Exception e ) {
                // skip
            }
            try {
                uCache = (HashMap<?, ?>) getFieldObject( factory, "urlCache", null );
            } catch ( Exception e ) {
                // skip
            }
            if ( uCache != null ) {
                Set<?> set = null;
                while ( set == null ) {
                    try {
                        set = ( (HashMap<?, ?>) uCache.clone() ).keySet();
                    } catch ( ConcurrentModificationException e ) {
                        //Fix for BACKLOG-2149 - Do nothing - while loop will try again.
                    }
                }

                for ( Object file : set ) {
                    if ( file instanceof JarFile ) {
                        JarFile jar = (JarFile) file;
                        if ( !closedFiles.contains( jar.getName() ) ) {
                            continue;
                        }
                        try {
                            jar.close();
                        } catch ( IOException e ) {
                            // skip
                        }
                        if ( fCache != null ) {
                            fCache.remove( uCache.get( jar ) );
                        }
                        uCache.remove( jar );
                    }
                }
            } else if ( fCache != null ) {
                for ( Object key : ( (HashMap<?, ?>) fCache.clone() ).keySet() ) {
                    Object file = fCache.get( key );
                    if ( file instanceof JarFile ) {
                        JarFile jar = (JarFile) file;
                        if ( !closedFiles.contains( jar.getName() ) ) {
                            continue;
                        }
                        try {
                            jar.close();
                        } catch ( IOException e ) {
                            // ignore
                        }
                        fCache.remove( key );
                    }
                }
            }
        } catch ( Exception e ) {
            // skip
            e.printStackTrace();
        }
    }

    @Override
    public URL getResource( String name ) {
        URL url;
        url = findResource( name );
        if ( url == null && getParent() != null ) {
            url = getParent().getResource( name );
        }
        return url;
    }
}
