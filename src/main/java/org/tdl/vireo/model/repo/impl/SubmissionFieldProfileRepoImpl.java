package org.tdl.vireo.model.repo.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.tdl.vireo.model.ControlledVocabulary;
import org.tdl.vireo.model.FieldGloss;
import org.tdl.vireo.model.FieldProfile;
import org.tdl.vireo.model.SubmissionFieldProfile;
import org.tdl.vireo.model.repo.SubmissionFieldProfileRepo;
import org.tdl.vireo.model.repo.custom.SubmissionFieldProfileRepoCustom;

import edu.tamu.weaver.data.model.repo.impl.AbstractWeaverRepoImpl;

public class SubmissionFieldProfileRepoImpl extends AbstractWeaverRepoImpl<SubmissionFieldProfile, SubmissionFieldProfileRepo> implements SubmissionFieldProfileRepoCustom {

    @Autowired
    private SubmissionFieldProfileRepo submissionFieldProfileRepo;

    @Override
    @Transactional // this is needed to lazy fetch fieldGlosses and controlledVocabularies
    public SubmissionFieldProfile create(FieldProfile fieldProfile) {

        SubmissionFieldProfile submissionfieldProfile = submissionFieldProfileRepo.findByFieldPredicateAndInputTypeAndRepeatableAndOptionalAndHiddenAndLoggedAndUsageAndHelpAndMappedShibAttributeAndFlaggedAndDefaultValueAndEnabled(fieldProfile.getFieldPredicate(), fieldProfile.getInputType(), fieldProfile.getRepeatable(), fieldProfile.getOptional(), fieldProfile.getHidden(), fieldProfile.getLogged(), fieldProfile.getUsage(), fieldProfile.getHelp(), fieldProfile.getMappedShibAttribute(), fieldProfile.getFlagged(), fieldProfile.getDefaultValue(), fieldProfile.getEnabled());

        if (submissionfieldProfile == null) {
            submissionfieldProfile = new SubmissionFieldProfile();

            submissionfieldProfile.setFieldPredicate(fieldProfile.getFieldPredicate());

            submissionfieldProfile.setControlledVocabularies(new ArrayList<ControlledVocabulary>(fieldProfile.getControlledVocabularies()));
            submissionfieldProfile.setFieldGlosses(new ArrayList<FieldGloss>(fieldProfile.getFieldGlosses()));
            submissionfieldProfile.setHelp(fieldProfile.getHelp());
            submissionfieldProfile.setInputType(fieldProfile.getInputType());
            submissionfieldProfile.setOptional(fieldProfile.getOptional());
            submissionfieldProfile.setHidden(fieldProfile.getHidden());
            submissionfieldProfile.setFlagged(fieldProfile.getFlagged());
            submissionfieldProfile.setLogged(fieldProfile.getLogged());
            submissionfieldProfile.setRepeatable(fieldProfile.getRepeatable());
            submissionfieldProfile.setUsage(fieldProfile.getUsage());
            submissionfieldProfile.setMappedShibAttribute(fieldProfile.getMappedShibAttribute());
            submissionfieldProfile.setDefaultValue(fieldProfile.getDefaultValue());
            submissionfieldProfile.setEnabled(fieldProfile.getEnabled());

            submissionfieldProfile = submissionFieldProfileRepo.save(submissionfieldProfile);
        }

        return submissionfieldProfile;
    }

    @Override
    protected String getChannel() {
        return "/channel/submission-field-profile";
    }

}
