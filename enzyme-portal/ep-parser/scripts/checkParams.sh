#!/bin/bash

case $1 in
	uzpdev|uzprel);;
	*) echo 'runtime environment (uzpdev|uzprel) required as $1' && exit 1;;
esac
