package csci572hw2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
;



public class WordCounter {

	static class WordCountMapper extends Mapper<Object,Text,Text,Text>{
		
		private Text word=new Text();
		private Text ids=new Text();
		public void map(Text key,Text value,Context context) 
				throws IOException,InterruptedException{
			 String line = value.toString();
			 int i=0;
			 for(;i<line.length();i++){
				 if(line.charAt(i)=='\t'){
					 break;
				 }
			 }
			 String id=line.substring(0,i);
			 ids.set(id);
			 StringTokenizer tokenizer=new StringTokenizer(line.substring(i+1,line.length()));
			 while(tokenizer.hasMoreTokens()){
				 word.set(tokenizer.nextToken());
				 context.write(word,ids);
			 }
		}
		
	}
	
	static class WordCountReducer extends Reducer<Text,Text,Text,Text>{
		public void reduce(Text key,Iterable<Text> values,Context context)
				throws IOException,InterruptedException{
			Map<String,Integer> map=new HashMap<String,Integer>();
			for(Text value:values){
				String keyStr=value.toString();
				if(map.containsKey(keyStr)==false){
					map.put(keyStr,1);
				}
				else{
					map.put(keyStr,map.get(keyStr)+1);
				}
			}
			StringBuilder sb=new StringBuilder();
			for(String str:map.keySet()){
				sb.append(str+":"+map.get(str)+"\t");
			}
			Text res=new Text();
			res.set(sb.toString());
			context.write(key, res);
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		Job job=new Job();
		job.setJarByClass(WordCounter.class);
		job.setJobName("word count");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.waitForCompletion(true);
	}
	
}
