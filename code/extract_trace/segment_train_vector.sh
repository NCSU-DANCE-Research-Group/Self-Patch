#!/bin/bash
for dir in ./*
do
    test -d "$dir" || continue
    echo "dir: $dir"
    pushd $dir
    filenames=$(ls *.csv | head -1)
    filename=${filenames[0]}
    #echo "filename: $filename"
    line_num=$(wc -l < $filename)
    #echo "line num: $line_num"
    line_num=$((line_num / 2))
    #echo "out line num: $line_num"
    #echo "out filename: ${filename//_test/}"
    head -n $line_num $filename > ${filename//_test/}
    echo "created: ${filename//_test/}"
    popd
done
