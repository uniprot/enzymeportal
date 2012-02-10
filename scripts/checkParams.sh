#!/bin/bash

case $1 in
	enzdev|ezprel);;
	*) echo 'runtime environment (enzdev|ezprel) required as $1' && exit 1;;
esac
