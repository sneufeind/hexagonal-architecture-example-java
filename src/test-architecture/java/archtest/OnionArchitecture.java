package archtest;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.library.Architectures;

import java.util.*;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class OnionArchitecture implements ArchRule {

    private enum LayerType {
        APPLICATION,
        DOMAIN,
        INFRASTRUCTURE
    }

    private Map<LayerType, Collection<String>> layerIdentifiers = new HashMap<>();

    public OnionArchitecture(){
        initLayerIdentifiers(this.layerIdentifiers);
    }

    private static void initLayerIdentifiers(final Map<LayerType, Collection<String>> layerIdentifierMap){
        Arrays.asList(LayerType.values())
                .forEach(layerType -> layerIdentifierMap.put(layerType, new ArrayList<>()));
    }

    private void addLayerIdentifier(final String packageName, final LayerType layerId){
        this.layerIdentifiers.get(layerId).add(packageName);
    }

    public OnionArchitecture applicationPackage(final String packageName){
        addLayerIdentifier(packageName, LayerType.APPLICATION);
        return this;
    }

    public OnionArchitecture domainPackage(final String packageName){
        addLayerIdentifier(packageName, LayerType.DOMAIN);
        return this;
    }

    public OnionArchitecture infrastructurePackage(final String packageName){
        addLayerIdentifier(packageName, LayerType.INFRASTRUCTURE);
        return this;
    }

    private static String[] asArray(final Collection<String> list){
        return list.toArray(new String[list.size()]);
    }

    private static void layer(final Architectures.LayeredArchitecture rule, final Map<LayerType, Collection<String>> layerMap, final LayerType layerType){
        rule.layer(layerType.name()).definedBy(asArray(layerMap.get(layerType)));
    }

    private Architectures.LayeredArchitecture layeredArchitectureDelegate() {
        final Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture();
        layer(layeredArchitecture, this.layerIdentifiers, LayerType.APPLICATION);
        layer(layeredArchitecture, this.layerIdentifiers, LayerType.DOMAIN);
        layer(layeredArchitecture, this.layerIdentifiers, LayerType.INFRASTRUCTURE);
        layeredArchitecture
                .whereLayer(LayerType.DOMAIN.name()).mayOnlyBeAccessedByLayers(LayerType.APPLICATION.name(), LayerType.INFRASTRUCTURE.name())
                .whereLayer(LayerType.APPLICATION.name()).mayOnlyBeAccessedByLayers(LayerType.INFRASTRUCTURE.name())
                .whereLayer(LayerType.INFRASTRUCTURE.name()).mayNotBeAccessedByAnyLayer()
                .withOptionalLayers(false)
        ;
        return layeredArchitecture;
    }

    @Override
    public ArchRule as(final String newDescription) {
        return layeredArchitectureDelegate().as(newDescription);
    }

    @Override
    public ArchRule because(final String reason) {
        return layeredArchitectureDelegate().because(reason);
    }

    @Override
    public void check(final JavaClasses classes) {
        layeredArchitectureDelegate().check(classes);
    }

    @Override
    public EvaluationResult evaluate(final JavaClasses classes) {
        return layeredArchitectureDelegate().evaluate(classes);
    }

    @Override
    public String getDescription() {
        return layeredArchitectureDelegate().getDescription();
    }
}
