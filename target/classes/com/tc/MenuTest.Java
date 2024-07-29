import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuTest {

    @Test
    public void testMenuInputBinaryLanguage() {
        // Prepare input
        String input = "S\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Create Menu instance
        Menu menu = new Menu();

        // Call menu method
        menu.menu();

        // Assert the expected output
        // TODO: Add assertions for the expected output
    }

    @Test
    public void testMenuInputNonBinaryLanguage() {
        // Prepare input
        String input = "N\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Create Menu instance
        Menu menu = new Menu();

        // Call menu method
        menu.menu();

        // Assert the expected output
        // TODO: Add assertions for the expected output
    }

    @Test
    public void testMenuInvalidInput() {
        // Prepare input
        String input = "Invalid\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Create Menu instance
        Menu menu = new Menu();

        // Call menu method
        menu.menu();

        // Assert the expected output
        // TODO: Add assertions for the expected output
    }
}