for file in *.txt; do
    iconv -f ISO-8859-9 -t utf-8 "$file" -o "_utf8_${file%.txt}.txt"
done