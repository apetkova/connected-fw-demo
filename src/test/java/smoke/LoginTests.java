package smoke;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests {

    @Test
    void correctCredentialsTest(){
        Assert.assertEquals(false, true);
    }

    @Test
    void incorrectCredentialsTest(){
        Assert.assertEquals(true, true);
    }
}
