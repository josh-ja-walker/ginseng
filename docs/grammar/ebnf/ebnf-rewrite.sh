#!/bin/bash


filename=$1
if [ -e "$filename" ]; then
    content=$(cat "$filename")
    # echo "$content"
    
else 
    echo "File not found: $filename"
fi


echo "$content" > $3

case "$2" in 

"--wirth")
    # Replace rules:
    sed -i -e 's/(\* /# /g' $3 # Replace (* ... *) comments with # ...
    sed -i -e 's/\*)//g' $3 # Delete trailing comment
    
    sed -i -e 's/;/./g' $3 # Replace semi-colon endings with full-stops
    sed -i -e 's/=/->/g' $3 # Replace production definition = with ->
    sed -i -e 's/,//g' $3 # Delete commas between productions
    sed -i -e 's/\[/FIXME: \[/g' $3 # Fix optionals manually
;;
    
"--xml-style")
    # TODO:

;;
    
"--w3c")
    # Replace rules:
    sed -i -e 's/(\*.*\*)//g' $3 # Replace (* ... *) comments with # ...
    sed -i -e 's/\*)//g' $3 # Delete trailing comment
    
    sed -i -e 's/;//g' $3 # Delete semi-colon endings
    sed -i -e 's/=/::=/g' $3 # Replace production definition = with ::=
    sed -i -e 's/,//g' $3 # Delete commas between productions
    
    sed -i -e 's/\[/(/g' $3 # Replace start of optional [ with (
    sed -i -e 's/\]/)?/g' $3 # Replace end of optional ] with )?
    
    sed -i -e 's/{/(/g' $3 # Replace start of repetition { with (
    sed -i -e 's/}/)*/g' $3 # Replace end of repetition } with )?
;;

esac


