# Dataflow Frame

Dataflow Frame是基于kettle的设计思想开发而来的一个j基础框架。

core是一个抽象层，可以集成任意能够组件化，流程化的计算引擎或者调度框架。

通过web界面以组件拖拉拽的形式，完成可视化操作。

替代传统手动写代码或者编写配置文件的形式。

可以基于spark，flink作为计算引擎，开发etl的组件或者算法模型的组件。

**ps:  支持复杂操作！！！而不是只支持读写！！！ 支持可视化！！！ 而不是写配置文件！！！**

可以基于azkban作为调度引擎，开发工作流的组件。

通过spark，flink，azkaban插件可以替代kettle的job和step插件。

让kettle和大数据有机结合，做到既有kettle的强大功能又有大数据的强大性能。

同时，基于统一的抽象层，可以完成插件的统一管理等功能。

spark，flink，azkaban的集成端及web端闭源开发。

spark，azkban主体功能开发完成。

## 插件列表

1. https://gitee.com/dufafei/dataflow-datasource-plugin.git

2. https://gitee.com/dufafei/dataflow-engine-spark-plugin.git

3. https://gitee.com/dufafei/dataflow-engine-azkaban-plugin.git 

## 亮点

1 解析方式可扩展。框架定义了基于前端mxGraph的解析方式。你也可以自定义。

2 插件可扩展，需要定义组件的注解类型。

3 插件的组件通过指定的注解扩展，简单方便。框架自动识别注册。

3 框架和组件隔离，动态加载

ps: 框架和组件是两个独立项目，因为我觉得框架需要和应用隔离，而组件的实现我认为偏向应用

4 可和其余框架集成。

5 可以支持数据血缘。

**QQ交流群：84457011**

