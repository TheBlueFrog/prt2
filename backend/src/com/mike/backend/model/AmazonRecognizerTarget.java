package com.mike.backend.model;

import com.mike.backend.Variables;
import com.mike.backend.db.AbstractMessageNode;
import com.mike.backend.db.JSONMessageNode;
import org.json.simple.JSONObject;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 9/6/2016.
 */
public class AmazonRecognizerTarget extends AbstractRecognizerTarget {

    public AmazonRecognizerTarget(String verb, String[] variables) {
        super(verb, Arrays.asList(variables));
    }

    // table to map the existing Amazon Alexa variable names
    // to the names we use here, could change the Alexa code...

    static private Map<String,String> amazonToCommon = new HashMap<>();
    static {
        amazonToCommon.put("WHO",       Variables.requestVarWho);
        amazonToCommon.put("WHAT",      Variables.requestVarWhat);
        amazonToCommon.put("WHEN",      Variables.requestVarWhen);
        amazonToCommon.put("QUANTITY",  Variables.requestVarQuantity);
        amazonToCommon.put("UNITS",     Variables.requestVarUnits);
    }

    @Override
    public boolean matches(AbstractMessageNode amn) {

        this.message = amn;
        JSONMessageNode m = (JSONMessageNode) amn;

        clearMatches();

        JSONObject j = m.getJSON();
        for(Object s : j.keySet())
            if (s instanceof String) {
                String k = (String) s;
                String mappedK = amazonToCommon.get(k);
                if (variables.containsKey(mappedK)) {
                    if (mappedK.equals(Variables.requestVarWhen))
                        variables.put(mappedK, amazonDate((String) j.get(k)));
                    else
                        variables.put(mappedK, (String) j.get(k));
                }
            }

        return true;
    }


    // everything in this class is static data, except the variable map,
    // the map contain the recognizer targets and any captured variable
    // values, make sure that gets copied
    public AmazonRecognizerTarget(AmazonRecognizerTarget r) {
        super(r.verb, null);

        this.variables = new HashMap<String, String>(r.variables);
//        for (String s : r.variables.keySet())
//            this.variables.put(s, r.variables.get(s));
    }

    @Override
    public AbstractRecognizerTarget copy() {
        return new AmazonRecognizerTarget(this);
    }

    /*
    AMAZON.DATE
    Converts words that represent dates into a date format.

    The date is provided to your service in ISO-8601 date format. Note that the date
    your service receives in the slot can vary depending on the specific phrase
    uttered by the user:

        Utterances that map to a specific date (such as “today”, or “november
        twenty-fifth”) convert to a complete date: 2015-11-25.

        Utterances that map to just a specific week (such as “this week” or “next week”),
        convert a date indicating the week number: 2015-W49.

        Utterances that map to the weekend for a specific week (such as “this weekend”)
        convert to a date indicating the week number and weekend: 2015-W49-WE.

        Utterances that map to a month, but not a specific day (such as “next month”,
        or “december”) convert to a date with just the year and month: 2015-12.

        Utterances that map to a year (such as “next year”) convert to a date containing
        just the year: 2016.

        Utterances that map to a decade convert to a date indicating the decade: 201X.

        Utterances that map to a season (such as “next winter”) convert to a date with
        the year and a season indicator: winter: WI, spring: SP, summer: SU, fall: FA)
     */

    // ToDo lots of work here to get this right...try to get into either
    // classic Java timestamp or look into Instant...

    private String amazonDate(String s) {
        Date date = Date.from( ZonedDateTime.parse(s).toInstant() );
        return s;//date.toString();
    }
}
