/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.transcoder;

// [START transcoder_create_job_from_preset]

import com.google.cloud.video.transcoder.v1.CreateJobRequest;
import com.google.cloud.video.transcoder.v1.Job;
import com.google.cloud.video.transcoder.v1.LocationName;
import com.google.cloud.video.transcoder.v1.TranscoderServiceClient;
import java.io.IOException;

public class CreateJobFromPreset {

  public static void main(String[] args) throws Exception {
    // TODO(developer): Replace these variables before running the sample.
    String projectId = "my-project-id";
    String location = "us-central1";
    String inputUri = "gs://my-bucket/my-video-file";
    String outputUri = "gs://my-bucket/my-output-folder/";
    // See https://cloud.google.com/transcoder/docs/concepts/overview#job_template
    // for information on this preset.
    String preset = "preset/web-hd";

    createJobFromPreset(projectId, location, inputUri, outputUri, preset);
  }

  // Creates a job from a preset.
  public static void createJobFromPreset(
      String projectId, String location, String inputUri, String outputUri, String preset)
      throws IOException {
    // Initialize client that will be used to send requests. This client only needs to be created
    // once, and can be reused for multiple requests.
    try (TranscoderServiceClient transcoderServiceClient = TranscoderServiceClient.create()) {

      CreateJobRequest createJobRequest =
          CreateJobRequest.newBuilder()
              .setJob(
                  Job.newBuilder()
                      .setInputUri(inputUri)
                      .setOutputUri(outputUri)
                      .setTemplateId(preset)
                      .build())
              .setParent(LocationName.of(projectId, location).toString())
              .build();

      // Send the job creation request and process the response.
      Job job = transcoderServiceClient.createJob(createJobRequest);
      System.out.println("Job: " + job.getName());
    }
  }
}
// [END transcoder_create_job_from_preset]
