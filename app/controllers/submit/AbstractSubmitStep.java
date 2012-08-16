package controllers.submit;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.tdl.vireo.model.*;
import org.tdl.vireo.state.State;

import com.google.gson.Gson;

import controllers.AbstractVireoController;
import controllers.Authentication;
import controllers.Student;
import controllers.submit.PersonalInfo;

import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import sun.util.logging.resources.logging;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import play.mvc.Scope.Params;
import static org.tdl.vireo.model.Configuration.CURRENT_SEMESTER;
import static org.tdl.vireo.model.Configuration.SUBMISSIONS_OPEN;

/**
 * THIS CONTROLLER IS BEING REFACTORED. 
 * 
 * Please don't touch right now.
 */


@With(Authentication.class)
public class AbstractSubmitStep extends AbstractVireoController {
	
	/**
	 * Set up values needed to display submission status at the top of each page.
	 * If submissions are closed - always redirect to the submissionStatus page. 
	 */
	
	@Before(unless="submissionStatus")
	static void beforeSubmit() {
		
		if (settingRepo.findConfigurationByName(SUBMISSIONS_OPEN) == null)
			Student.submissionStatus();	
		
		renderArgs.put("SUBMISSIONS_OPEN", settingRepo.findConfigurationByName(SUBMISSIONS_OPEN));
		
		Configuration curSemConfig = settingRepo.findConfigurationByName(CURRENT_SEMESTER);
		String currentSemester = (curSemConfig == null ? "undefined" : curSemConfig.getValue());
			
		renderArgs.put("CURRENT_SEMESTER", currentSemester);


	}
	
	protected static Submission getSubmission() {
		Long subId = params.get("subId", Long.class);
		Submission sub = subRepo.findSubmission(subId);
		
		if (sub == null) {
		    error("Did not receive the expected submission id.");
		} 
				
		// This is an existing submission so check that we're the student here.
		Person submitter = context.getPerson();
		if (sub.getSubmitter() != submitter)
		    unauthorized();
		
		return sub;
	}
}