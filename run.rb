#!/usr/bin/env ruby

@algorithm = ARGV[0]
@config = ARGV[1] || 'myconfig'

def print_usage
  puts 'Usage: ./run.rb ALGORITHM [CONFIG]'
  puts "  available algorithms:\n    #{available_algorithms.join("\n    ")}"
  puts "  available configs:\n    #{available_configs.join("\n    ")}"
  true
end

def available_algorithms
  Dir['assignment4/*'].map { |file| File.basename file }
end

def available_configs
  Dir["assignment4/#{@algorithm}/*.txt"].map { |file| File.basename file, '.*' }
end

def print_and_run(cmd)
  puts cmd
  system cmd
end

print_usage && exit(1) unless available_algorithms.include? @algorithm
print_usage && exit(1) if !@config.nil? && !available_configs.include?(@config)

print_and_run 'java'\
              ' -jar teachnet-1.0.3/teachnet.jar'\
              " --cp assignment4/#{@algorithm}"\
              " --config assignment4/#{@algorithm}/#{@config}.txt"\
              ' --compile'
