#!/bin/bash

case $1 in
	vezpdev|ezprel);;
	*) echo 'runtime environment (vezpdev|ezprel) required as $1' && exit 1;;
esac
