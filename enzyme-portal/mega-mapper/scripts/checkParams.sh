#!/bin/bash

case $1 in
	dev|test|prod);;
	*) echo 'runtime environment (dev|test|prod) required as $1' && exit 1;;
esac
