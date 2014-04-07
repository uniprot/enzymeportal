#!/bin/bash

case $1 in
	enzdev|ezprelvm);;
	*) echo 'runtime environment (enzdev|ezprel) required as $1' && exit 1;;
esac
