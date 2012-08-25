#!/bin/bash

echo "$1 Jetty Server..."
./runserver.sh com.sqewd.open.dal.server.JettyServer -cmd $1 -config ./src/test/java/com/sqewd/open/dal/demo/config/server-demo.xml
