package com.example.bajajtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService implements CommandLineRunner{
    
    @Autowired
    private RestTemplate restTemplate;

    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    @Override
    public void run(String... args) throws Exception {
        // Step 1: Generate Webhook
        InitialRequestDTO initialRequest = new InitialRequestDTO("John Doe", "2211201322", "john.doe@example.com");
        InitialResponseDTO initialResponse = restTemplate.postForObject(GENERATE_WEBHOOK_URL, initialRequest, InitialResponseDTO.class);

        if (initialResponse != null && initialResponse.getWebhook() != null && initialResponse.getAccessToken() != null) {
            System.out.println("Webhook URL: " + initialResponse.getWebhook());
            System.out.println("Access Token: " + initialResponse.getAccessToken());

            // Step 2: Solve SQL and Submit
            String finalQuery = "WITH EmployeeAges AS ( " +
                    "SELECT " +
                    "    e.EMP_ID, " +
                    "    e.FIRST_NAME, " +
                    "    e.LAST_NAME, " +
                    "    d.DEPARTMENT_NAME, " +
                    "    e.DOB, " +
                    "    d.DEPARTMENT_ID " +
                    "FROM " +
                    "    EMPLOYEE e " +
                    "JOIN " +
                    "    DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                    ") " +
                    "SELECT " +
                    "    ea1.EMP_ID, " +
                    "    ea1.FIRST_NAME, " +
                    "    ea1.LAST_NAME, " +
                    "    ea1.DEPARTMENT_NAME, " +
                    "    (SELECT COUNT(*) " +
                    "     FROM EmployeeAges ea2 " +
                    "     WHERE ea2.DEPARTMENT_ID = ea1.DEPARTMENT_ID AND ea2.DOB > ea1.DOB) AS YOUNGER_EMPLOYEES_COUNT " +
                    "FROM " +
                    "    EmployeeAges ea1 " +
                    "ORDER BY " +
                    "    ea1.EMP_ID DESC;";


            FinalRequestDTO finalRequest = new FinalRequestDTO(finalQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // headers.set("Authorization", "Bearer " + initialResponse.getAccessToken());
            headers.set("Authorization", initialResponse.getAccessToken());

            HttpEntity<FinalRequestDTO> entity = new HttpEntity<>(finalRequest, headers);

            String submissionUrl = initialResponse.getWebhook();
            String result = restTemplate.postForObject(submissionUrl, entity, String.class);

            System.out.println("Submission Result: " + result);
        } else {
            System.err.println("Failed to retrieve webhook URL and access token.");
        }
    }
}
