package org.atom.quark.jdbc.helper;

import org.atom.quark.jdbc.context.JdbcContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A JdbcHelper using Spring's JdbcTemplate to perform its work.
 * @author Pierre Beucher
 *
 */
public class SpringJdbcHelper extends JdbcHelper {
	
	private JdbcTemplate template;

	public SpringJdbcHelper(JdbcContext context) {
		super(context);
		this.template = new JdbcTemplate(context.getDataSource());
	}

	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

}
