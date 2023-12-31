/*
 * Copyright 2023 Google LLC
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

package com.example.prometheus;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PrometheusApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testMetrics() throws Exception {
    for (int time : Lists.list(847, 904, 978, 473, 562, 262, 376, 99, 298, 302, 800)) {
      mockMvc
          .perform(get("/"))
          .andExpect(status().isOk())
          .andExpect(content().string("Succeeded after " + time + "ms."));
    }
    mockMvc
        .perform(get("/"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Intentional failure encountered!"));
    mockMvc
        .perform(get("/metrics"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("java_request_count_total 12.0")))
        .andExpect(content().string(containsString("java_failed_request_count_total 1.0")))
        .andExpect(
            content().string(containsString("java_response_latency_bucket{le=\"0.5\",} 7.0")));

  }

  @TestConfiguration
  public static class TestConfig {
    @Bean
    @Primary
    Random deterministicRandom() {
      // deterministic random
      return new Random(1L);
    }
  }
}
