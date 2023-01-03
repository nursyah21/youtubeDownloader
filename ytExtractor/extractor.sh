# download url
url=$1

curl -H "User-Agent: Mozilla" \
   -H "Accept-Encoding: gzip, deflate, br" \
   -L $url > .temp
  
python utils.py decompress .temp

du -h .*
