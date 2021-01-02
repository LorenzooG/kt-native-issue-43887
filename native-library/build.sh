#!/bin/bash

cmake -G "Unix Makefiles" -S . -B build

cd build || exit 1

make

cd ..

echo "Success compiled kofl runtime."