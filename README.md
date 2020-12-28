# Dataflow Frame

Dataflow Frame是基于kettle的设计思想开发而来的一个框架。

基于Dataflow Frame可以无缝接入现有的框架，包含azkaban，spark，flink，datax等等。

1.  数据源（已实现，已开源）

   dataflow-datasource-plugin：https://gitee.com/dufafei/dataflow-datasource-plugin.git

2. engine-azkaban（已实现，已开源）

   dataflow-engine-azkaban：https://gitee.com/dufafei/dataflow-engine-azkaban

   dataflow-engine-azkaban-plugin：https://gitee.com/dufafei/dataflow-engine-azkaban-plugin

   使用方法：dataflow-engine-azkaban-plugin项目先package，参考test目录。

3. engine-spark（已实现，已开源）

   dataflow-engine-spark：https://gitee.com/dufafei/dataflow-engine-spark.git

   dataflow-engine-spark-plugin：https://gitee.com/dufafei/dataflow-engine-spark-plugin.git

4. engine-flink（开发中）

5. engine-datax（未开发）

可以接入web, 通过界面拖拉拽，界面配置组件参数的方式完成工作流的配置。

1. 前端（开发中，使用vue）：

2. 后端（开发中，使用spring-boot）：

   ps: 空余时间全花在这上面了，可能后续会暂停段时间，打算学下java基础（半路出家），还有大数据的其他东东。

在我看来，Dataflow Frame更像一个粘合剂。使用可以参考azkban，spark的实现。由于是业余时间开发，进度要稍慢。。。

亮点：

1 解析方式可扩展。框架定义了基于前端mxGraph的解析方式。你也可以自定义。

2 插件可扩展，需要定义组件的注解类型。

3 插件的组件通过指定的注解扩展，简单方便。框架自动识别注册。

3 框架和组件隔离（框架和组件是两个独立项目，因为我觉得框架需要和应用隔离，而组件的实现我认为偏向应用）。框架通过添加指定目录的方式，动态加载组件。

4 可和其余框架集成。

5 集成spark，flink，datax的同时可以支持数据血缘。

### Project Structure

- **meta:** 把一个流程拆抽象为VertextMeta,EdgeMeta,FlowMeta3个对象。
- **resolver:**  目前定义了前端mxGraph方式的解析，可以和web打通，当然你也可以自定义解析方式，比如json。把xml格式或者json格式的流程描述文件解析为meta对象。
- **plugin:** 定义插件类型的基类，通过基类扩展插件类型，通过插件类型可以定义该插件对应组件的注解和添加组件目录。通过插件扩展组件。



QQ交流群：84457011

