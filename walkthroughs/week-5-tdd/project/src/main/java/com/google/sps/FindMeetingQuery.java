// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.sql.Time;
import java.util.List;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> unavailabeTimeRanges = getScheduleOfUnavailableTimes(events, request.getAttendees());
    List<TimeRange> availableTimeRanges = new ArrayList<>();
    int startTime = TimeRange.START_OF_DAY;
    long duration = request.getDuration();
    for(TimeRange unavailableTimeRange: unavailabeTimeRanges){
      TimeRange availableTimeRange = TimeRange.fromStartEnd(startTime, unavailableTimeRange.start(), false);
      if(availableTimeRange.duration() >= duration){
        availableTimeRanges.add(availableTimeRange);
      }
      startTime = unavailableTimeRange.end();
    }
    if(!availableTimeRanges.isEmpty() || unavailabeTimeRanges.isEmpty()){
      TimeRange availableTimeRange = TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true);
      if(availableTimeRange.duration() >= duration){
        availableTimeRanges.add(availableTimeRange);
      }
    }
    return availableTimeRanges;
  }

  public List<TimeRange> getScheduleOfUnavailableTimes(Collection<Event> events, Collection <String> attendees) {
    Set<Event> conflictingEvents = findAllConflictingEvents(events, attendees);
    // Once we have all the conflicting events, we convert them into time ranges
    List<TimeRange> unavailabeTimeRanges = convertEventsToTimeRanges(conflictingEvents);
    return unavailabeTimeRanges;
  }

  public Set<Event> findAllConflictingEvents(Collection<Event> events, Collection<String> attendees){
    Iterator<Event> eventsIterator = events.iterator();
    // This set will contains any events attended by attendees in requested meeting
    Set<Event> conflictingEvents = new HashSet<>();
    // Iterate through all event-attendee pairs
    while(eventsIterator.hasNext()){
      Event event = eventsIterator.next();
      Iterator<String> attendeesIterator = attendees.iterator();
      while(attendeesIterator.hasNext()){
        String attendee = attendeesIterator.next();
        // Add any event that contains at least one attendee in the requested meeting
        if(event.getAttendees().contains(attendee)){
          conflictingEvents.add(event);
          break;
        }
      }
    }
    return conflictingEvents;
  }

  public List<TimeRange> convertEventsToTimeRanges(Collection<Event> events){
    List<TimeRange> eventTimeRanges = new ArrayList<>();
    Iterator<Event> eventsIterator = events.iterator();
    while(eventsIterator.hasNext()){
      Event event = eventsIterator.next();
      TimeRange eventTimeRange = event.getWhen();
      eventTimeRanges.add(eventTimeRange);
    }
    return mergeTimeRange(eventTimeRanges);
  }

  /**
   * This method will add a time range to a list if it does not overlap with any other time ranges.
   * Otherwise, it will find the time range it overlaps with and merge with it.
   * @param timeRanges The list of TimeRanges.
   * @param timeRangeToMerge The TimeRange to be merged.
   * @return An array of sorted and merged time ranges.
   */
  public List<TimeRange> mergeTimeRange(List<TimeRange> timeRanges){
    ArrayDeque<TimeRange> merged = new ArrayDeque<>();
    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);
    for(TimeRange timeRangeToMerge: timeRanges){
      if(!merged.isEmpty() && timeRangeToMerge.overlaps(merged.peek())){
        TimeRange timeRange = merged.poll();
        // take earlier start time
        int newStartTime = timeRange.start() < timeRangeToMerge.start() ? timeRange.start() : timeRangeToMerge.start();
        // take later end time
        int newEndTime = timeRange.end() > timeRangeToMerge.end() ? timeRange.end() : timeRangeToMerge.end();
        merged.push(TimeRange.fromStartEnd(newStartTime, newEndTime, false));
      }
      else{
        merged.push(timeRangeToMerge);
      }
    }
    List<TimeRange> sortedAndMerged = new ArrayList<>();
    Iterator<TimeRange> descendingIterator = merged.descendingIterator();
    while(descendingIterator.hasNext()){
      TimeRange timeRange = descendingIterator.next();
      sortedAndMerged.add(timeRange);
    }
    return sortedAndMerged;
  }
}
