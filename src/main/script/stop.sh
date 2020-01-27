#!/usr/bin/env bash
ps -ef | grep @project.artifactId@
echo Entre com o ID do processo?
read varname
echo Parando API...
kill -9 $varname
ps -ef | grep @project.artifactId@
