package com.warnercodes.watchable;

import org.junit.Test;

import static com.warnercodes.watchable.Costants.API_KEY;
import static com.warnercodes.watchable.Costants.LANG;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void MovieTest(){
        Movie movie = new Movie();
        String url = "https://api.themoviedb.org/3/movie/181812?api_key=" + API_KEY + "&language=" + LANG + "";

        


    }
}