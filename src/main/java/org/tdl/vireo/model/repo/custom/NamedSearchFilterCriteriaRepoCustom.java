package org.tdl.vireo.model.repo.custom;

import org.tdl.vireo.model.NamedSearchFilterCriteria;
import org.tdl.vireo.model.User;

public interface NamedSearchFilterCriteriaRepoCustom {

    public NamedSearchFilterCriteria create(User user, String name);

}