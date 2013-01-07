PentahoEhCachePlugin
====================

Ehcache plugin for Pentaho PDI - Kettle 
This plugin can be used within Kettle (PDI) to access an ehcache / Terracotta instance. 

Please note, that this plugin is still in ALPHA stage - it works well for simple key/value pairs and has been tested. 
It is not yet considered stable, but "usable" :) ... Please use with caution. 

To install, please make sure that you:
* Install and start your Terracotta Server
* Install Kettle/PDI from here: http://www.pentaho.de/download/
* Unzip the ehCachePlugin_vXX.zip to the <Pentaho>/plugins/steps/ folder
* Place the terracotta-license.key inside the <Pentaho> classpath
* The distributed Terracotta server jar files work with Terracotta 3.7. (If you are using a different version
   please update overwrite the libraries with the ones from your Terracotta installation).
* Start Kettle through the spoon.sh and check the startup script for error log entries. 

Within Kettle, you should see a new design category called "Terracotta". Simply drag & drop the methods into your 
transformation and start using it. 

Have fun!
