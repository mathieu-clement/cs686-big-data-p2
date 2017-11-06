package edu.usfca.cs.mr.climate_chart;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * This is the main class. Hadoop will invoke the main method of this class.
 */
public class ClimateChartJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            conf.set("geohash_prefix", /*args[2]*/"dnd");

            Job job = Job.getInstance(conf, "climate_chart_job");// Current class.
            job.setJarByClass(ClimateChartJob.class);
            // Mapper
            job.setMapperClass(ClimateChartMapper.class);
            // Reducer
            job.setReducerClass(ClimateChartReducer.class);
            // Outputs from the Mapper.
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            // Outputs from Reducer. It is sufficient to set only the following
            // two properties if the Mapper and Reducer has same key and value
            // types. It is set separately for elaboration.
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            //LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
            // path to input in HDFS
            FileInputFormat.addInputPaths(job, args[0]);
            // path to output in HDFS
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            // Block until the job is completed.
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
