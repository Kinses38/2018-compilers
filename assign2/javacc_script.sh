#!/usr/bin/env bash
jjtree CalParser.jjt && javacc CalParser.jj && javac CalParser.java
