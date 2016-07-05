package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 23/02/2016.
 */
public class SetValueTest {

    private SetValue setValue;
    String xml;

    @Before
    public void setUp() throws Exception{
        setValue = new SetValue();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        setValue = null;
        xml = null;
    }

    @Test
    public void testSetElementValue() {
        String xPathQuery = "//subelement";
        String value = "test value";

        Map<String, String> result = setValue.execute(xml, "", xPathQuery, null, value, "false");

        assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.SET_VALUE_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testSetAttributeValue() {
        String xPathQuery = "//subelement";
        String name = "atName";
        String value = "test value";

        Map<String, String> result = setValue.execute(xml, "", xPathQuery, name, value, "false");

        assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.SET_VALUE_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String name = "atName";
        String value = "test value";

        Map<String, String> result = setValue.execute(xml, "", xPathQuery, name, value, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.ELEMENT_NOT_FOUND,
                result.get(Constants.Outputs.ERROR_MESSAGE));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String name = "atName";
        String value = "test value";

        Map<String, String> result = setValue.execute(xml, "", xPathQuery, name, value, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR +
                Constants.ErrorMessages.SET_VALUE_FAILURE + Constants.ErrorMessages.NEED_ELEMENT_TYPE,
                result.get(Constants.Outputs.ERROR_MESSAGE));
    }
}
