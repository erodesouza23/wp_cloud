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

package com.example.automl;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.TestCase.assertNotNull;

import com.google.cloud.testing.junit4.MultipleAttemptsRule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Ignore("This test is ignored because the legacy version of AutoML API is deprecated")
@RunWith(JUnit4.class)
@SuppressWarnings("checkstyle:abbreviationaswordinname")
public class ExportDatasetTest {
  @Rule public final MultipleAttemptsRule multipleAttemptsRule = new MultipleAttemptsRule(3);
  private static final String PROJECT_ID = System.getenv("AUTOML_PROJECT_ID");
  private static final String DATASET_ID = "TEN0000000000000000000";
  private static final String BUCKET_ID = PROJECT_ID + "-lcm";
  private static final String BUCKET = "gs://" + BUCKET_ID;
  private ByteArrayOutputStream bout;
  private PrintStream originalPrintStream;

  private static void requireEnvVar(String varName) {
    assertNotNull(
        System.getenv(varName),
        String.format("Environment variable '%s' is required to perform these tests.", varName));
  }

  @BeforeClass
  public static void checkRequirements() {
    requireEnvVar("GOOGLE_APPLICATION_CREDENTIALS");
    requireEnvVar("AUTOML_PROJECT_ID");
    requireEnvVar("ENTITY_EXTRACTION_DATASET_ID");
  }

  @Before
  public void setUp() {
    bout = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bout);
    originalPrintStream = System.out;
    System.setOut(out);
  }

  @After
  public void tearDown() {
    // restores print statements in the original method
    System.out.flush();
    System.setOut(originalPrintStream);
  }

  @Test
  public void testExportDataset() throws IOException, ExecutionException, InterruptedException {
    // As exporting a dataset can take a long time and only one operation can be run on a dataset
    // at once. Try to export a nonexistent dataset and confirm that the dataset was not found, but
    // other elements of the request were valid.
    try {
      ExportDataset.exportDataset(PROJECT_ID, DATASET_ID, BUCKET + "/TEST_EXPORT_OUTPUT/");
      String got = bout.toString();
      assertThat(got).contains("The Dataset doesn't exist or is inaccessible for use with AutoMl.");
    } catch (IOException | ExecutionException | InterruptedException e) {
      assertThat(e.getMessage())
          .contains("The Dataset doesn't exist or is inaccessible for use with AutoMl.");
    }
  }
}
