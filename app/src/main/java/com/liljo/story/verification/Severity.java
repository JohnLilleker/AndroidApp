package com.liljo.story.verification;

public enum Severity {
    ERROR, // Something that will stop the story from working i.e. unknown scene
    WARNING, // something you might want to look at, accidental e.g. scenes not being hit
    CAUTION, // something you might want to look at, intentional Cyclic
    PASS // all good
}
