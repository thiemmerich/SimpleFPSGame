package br.com.emmerich.utils;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

public class CustomEnvironment extends Environment {

    public CustomEnvironment() {
        super();

        // Set ambient light
        this.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));

        // Add directional light
        this.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1f, -0.5f));

        // Add specular lighting
//        this.add(new ColorAttribute(ColorAttribute.Specular, 0.2f, 0.2f, 0.2f, 1f));
    }
}
