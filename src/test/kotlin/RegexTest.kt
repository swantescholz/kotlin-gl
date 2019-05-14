import org.junit.Test
import util.extensions.println


class RegexTest {
	
	@Test
	fun testReges() {
		val regex = Regex("""(\d*:)(\d*)""")
		regex.replace("hello 02:3322 fdsofso xxx334:0022fseo", "$1|foodd").println()
	}
	
}