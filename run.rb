#!/usr/bin/env ruby

algorithm = ARGV[0]
system "java -jar teachnet-1.0.3/teachnet.jar --cp assignment1/#{algorithm} --config assignment1/#{algorithm}/myconfig.txt --compile"
