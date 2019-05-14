

//LIGHT + MATERIALS

vec4 yclamp(in vec4 color) {
	return clamp(color, cvec4zero, cvec4one);
}

vec4 normalToColor(vec3 normal) {
	return vec4(vec3(.5,.5,.5)+normalize(normal), 1.0);
}

void pointLight(in int i,
		inout vec4 ambient,
		inout vec4 diffuse,
		inout vec4 specular) {
	// vector from surface to light position
	vec3 L = uLightSource[i].position - vPosition;
	float d = length(L);     // distance between surface and light position
	L = normalize(L);
	vec3 R = normalize(-reflect(L, vNormal)); //perfect reflection vector
	vec3 E = normalize(uCamera.pos-vPosition); //to-eye vector
	float attenuation = 1.0 / (
		uLightSource[i].constantAttenuation +
		uLightSource[i].linearAttenuation * d +
		uLightSource[i].quadraticAttenuation * d * d);
	//direction of maximum highlights:
	//const vec3 halfVector = normalize(L + uCameraPosition-vPosition);
	float nDotL = max(0.0, dot(vNormal, L));
	float rDotE = max(0.0, dot(R,E));
	float pf; //power factor
	if (nDotL == 0.0) pf = 0.0;
	else pf = pow(rDotE, uMaterial.shininess);
	ambient += attenuation * uLightSource[i].ambient;
	diffuse += attenuation * nDotL * uLightSource[i].diffuse;
	specular += attenuation * pf * uLightSource[i].specular;
	//diffuse = mix(vec4(0,0,1,1),vec4(1,0,0,1), attenuation);
}
 
vec4 calcPerFragmentLighting() {
	vec4 amb = vec4(0.0);
	vec4 diff = vec4(0.0);
	vec4 spec = vec4(0.0);
	for (int i = 0; i < gl_MaxLights; i++) {
		if (uLightSource[i].enabled) {
			pointLight(i, amb, diff, spec);
		}
	}
	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	//color += uLightModelAmbient;
	color += yclamp(amb  * uMaterial.ambient) * 0.3;
	color += yclamp(diff * uMaterial.diffuse) * 0.7;
	color += yclamp(spec * uMaterial.specular) * 1.0;
	color = yclamp(color);
//	color.a = 1.0;
	return color;
}
