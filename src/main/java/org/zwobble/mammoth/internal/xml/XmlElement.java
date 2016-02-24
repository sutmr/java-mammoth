package org.zwobble.mammoth.internal.xml;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Iterables.*;

public class XmlElement implements XmlNode, XmlElementLike {
    private final String name;
    private final Map<String, String> attributes;
    private final List<XmlNode> children;
    
    public XmlElement(String name, Map<String, String> attributes, List<XmlNode> children) {
        this.name = name;
        this.attributes = attributes;
        this.children = children;
    }
    
    public String getName() {
        return name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getAttribute(String name) {
        // TODO: throw more informative error message
        return getAttributeOrNone(name).get();
    }

    @Override
    public Optional<String> getAttributeOrNone(String name) {
        return Optional.ofNullable(attributes.get(name));
    }

    public List<XmlNode> getChildren() {
        return children;
    }

    @Override
    public String innerText() {
        return Joiner.on("").join(transform(children, XmlNode::innerText));
    }

    @Override
    public <T> T accept(XmlNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Iterable<XmlNode> children() {
        return children;
    }

    @Override
    public String toString() {
        return "XmlElement(name=" + name + ", attributes=" + attributes
               + ", children=" + children + ")";
    }

    public XmlElementList findChildren(String name) {
        Iterable<XmlElement> filtered = findChildrenIterable(name);
        return new XmlElementList(ImmutableList.copyOf(filtered));
    }

    public XmlElement findChild(String name) {
        return findChildrenIterable(name).iterator().next();
    }

    @Override
    public boolean hasChild(String name) {
        return findChildrenIterable(name).iterator().hasNext();
    }

    @Override
    public XmlElementLike findChildOrEmpty(String name) {
        return getFirst(findChildrenIterable(name), NullXmlElement.INSTANCE);
    }

    private Iterable<XmlElement> findChildrenIterable(String name) {
        return filter(
            filter(children, XmlElement.class),
            child -> child.getName().equals(name));
    }
}