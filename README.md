PentahoEhCachePlugin
====================

Ehcache plugin for Pentaho PDI - Kettle 
This plugin can be used within Kettle (PDI) to access an ehcache / Terracotta instance. 

Please note, that this plugin is still in BETA stage - it has been tested, but it is not yet bug-free. 
It is not yet considered stable, but "usable" :) ... Please use with caution. 

To install, please make sure that you:
* Install and start your Terracotta Server
* Install Kettle/PDI from here: http://www.pentaho.de/download/
* Unzip the ehCachePlugin_vXX.zip to the <Pentaho>/plugins/steps/ folder
* The Ehcache/Terracotta base libaries are no longer included. Make sure to copy the "Terracotta / Ehcache" specific jar files into the <Pentaho>/plugins/steps/ folder. 
  This should include:
    - ehcache-core-x.x.x.jar
    - ehcache-terracotta.x.x.x.jar
    - slf4j-api-x.x.x.jar
    - slf4j-jdk14-x.x.x.jar
    - terracotta-toolkit-x.x-runtime-y.y.y.jar

* Place the terracotta-license.key inside the <Pentaho> classpath
* Start Kettle through the spoon.sh and check the startup script for error log entries. 

Within Kettle, you should see a new design category called "Terracotta". Simply drag & drop the methods into your 
transformation and start using it. 

Have fun!


Change log: 
* 26/02/2013 - Updated Terracotta libraries to 3.7.4
* 27/03/2013 - Added reflective Pojo PUT service to the stack. 
* 03/04/2013 - Added reflective Pojo GET service.

