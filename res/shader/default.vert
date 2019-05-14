
void main(void) {
	vec4 vertex = uModelMatrix * gl_Vertex;
	vNormal = mat3transformNormal(gl_Normal.xyz, uNormalMatrix);
	vTexCoord0 = gl_MultiTexCoord0.xy;
	vPosition = vertex.xyz;
	vertex = (uProjectionMatrix * (uViewMatrix * vertex));
	gl_Position = vertex;
}

