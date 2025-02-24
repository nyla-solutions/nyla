package nyla.solutions.spring.validation;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;

/**
 * Test case for OrREValidation
 * @author Gregory Green
 *
 */
public class OrREValidationTest
{

	@Test
	public void testOR()
	{
		OrREValidation or = new OrREValidation();
		or.addRegularExpresion(".*man.*");
		Errors errors = mock(Errors.class);
		
		or.validate("hello", errors);
		verify(errors).reject(anyString());
		
		Errors none = mock(Errors.class);
		
		or.validate("Imani", none);
		Mockito.verifyNoMoreInteractions(none);
		
	}

}
