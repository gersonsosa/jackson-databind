package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.BaseMapTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestCyclicDeserializer extends BaseMapTest {

    static class GrandChild {
        private String id;
        private Child child;

        public GrandChild() {
        }

        public GrandChild(String id) {
            this.id = id;
        }
    }

    static class Child {
        private String id;
        private Parent parent;
        private List<GrandChild> grandChildren;

        public Child() {
        }

        public Child(String id, List<GrandChild> grandChildren) {
            this.id = id;
            this.grandChildren = grandChildren;
        }
    }

    static class Parent {
        private List<Child> children;

        public Parent() {
        }

        public Parent(String id, List<Child> children) {
            this.children = children;
        }
    }

    public void testAddOrReplacePropertyIsUsedOnDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final String json = "{\"id\": 1, \"children\": [{\"id\": 2, \"parent\": 1, \"grandChildren\": [{\"id\": 21,\"child\": 2}, {\"id\": 22, \"child\": 2}]}, {\"id\": 3, \"grandChildren\": [{\"id\": 31, \"child\": 3}, {\"id\": 32, \"child\": 3}],\"parent\": 1}]}";
        Parent result = mapper.readValue(json, Parent.class);
        assertEquals("32", result.children.get(0).grandChildren.get(0).id);
    }
}
