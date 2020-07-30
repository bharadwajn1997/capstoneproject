package com.cognizant.capstoneprojectone;

import static org.junit.Assert.assertTrue;

import com.cognizant.capstoneprojectone.models.Feedback;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws JsonProcessingException {
        Feedback feedback = new Feedback();
        feedback.setEmail("test");
        feedback.setFeedback("test");
        feedback.setName("neha");
        feedback.setRating(Feedback.Rating.Average);

        String s = new ObjectMapper().writeValueAsString(feedback);
        System.out.println(s);

        assertTrue( true );
    }
}
