package com.privilist.model;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.IOException;


/**
 * Created by minhtdh on 6/22/15.
 */
@JsonDeserialize(using = Schedule.ScheduleDeserial.class)
public class Schedule {
    public Day monday;
    public Day tuesday;
    public Day wednesday;
    public Day thursday;
    public Day friday;
    public Day saturday;
    public Day sunday;
    //luanlq July 17,2015
    public Day returnDay(int i){
        switch (i){
            case 0:
                return monday;
            case 1:
                return tuesday;
            case 2:
                return wednesday;
            case 3:
                return thursday;
            case 4:
                return friday;
            case 5:
                return saturday;
            case 6:
                return sunday;
        }
        return null;
    }
    //luanlq July 17,2015 End.
    public static class ScheduleDeserial extends JsonDeserializer<Schedule> {
        @Override
        public Schedule deserialize(final JsonParser pJsonParser,
                                    final DeserializationContext pDeserializationContext) throws
                IOException,
                JsonProcessingException {
            JsonNode node = pJsonParser.readValueAsTree();
            Schedule ret = new Schedule();
            JsonNode child;
            child = node.findValue("Monday");
            if (child != null) {
                ret.monday = pJsonParser.getCodec().treeAsTokens(child).readValueAs(Day.class);
            }
            child = node.findValue("Tuesday");
            if (child != null) {
                ret.tuesday = pJsonParser.getCodec().treeAsTokens(child).readValueAs(Day.class);
            }
            child = node.findValue("Wednesday");
            if (child != null) {
                ret.wednesday = pJsonParser.getCodec().treeAsTokens(child).readValueAs(Day.class);
            }
            child = node.findValue("Thursday");
            if (child != null) {
                ret.thursday = pJsonParser.getCodec().treeAsTokens(child).readValueAs(Day.class);
            }
            child = node.findValue("Friday");
            if (child != null) {
                ret.friday = pJsonParser.getCodec().treeAsTokens(child).readValueAs(Day.class);
            }
            child = node.findValue("Saturday");
            if (child != null) {
                ret.saturday = pJsonParser.getCodec().treeAsTokens(child).readValueAs(Day.class);
            }
            child = node.findValue("Sunday");
            if (child != null) {
                ret.sunday = pJsonParser.getCodec().treeAsTokens(child).readValueAs(Day.class);
            }
            return ret;
        }
    }
}
