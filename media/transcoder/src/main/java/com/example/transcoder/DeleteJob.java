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

// [START transcoder_delete_job]

import com.google.cloud.video.transcoder.v1.DeleteJobRequest;
import com.google.cloud.video.transcoder.v1.JobName;
import com.google.cloud.video.transcoder.v1.TranscoderServiceClient;
import java.io.IOException;

public class DeleteJob {

  public static void main(String[] args) throws Exception {
    // TODO(developer): Replace these variables before running the sample.
    String projectId = "my-project-id";
    String location = "us-central1";
    String jobId = "my-job-id";

    deleteJob(projectId, location, jobId);
  }

  // Deletes a job.
  public static void deleteJob(String projectId, String location, String jobId) throws IOException {
    // Initialize client that will be used to send requests. This client only needs to be created
    // once, and can be reused for multiple requests.
    try (TranscoderServiceClient transcoderServiceClient = TranscoderServiceClient.create()) {
      JobName jobName =
          JobName.newBuilder().setProject(projectId).setLocation(location).setJob(jobId).build();
      DeleteJobRequest deleteJobRequest = DeleteJobRequest.newBuilder().setName(jobName.toString())
          .build();

      // Send the delete job request and process the response.
      transcoderServiceClient.deleteJob(deleteJobRequest);
      System.out.println("Deleted job");
    }
  }
}
// [END transcoder_delete_job]
