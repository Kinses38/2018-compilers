#!/usr/bin/env bash
for filename in ../test/*.cal; do
	java CalParser $filename
done
