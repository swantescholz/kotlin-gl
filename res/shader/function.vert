
void main(void) {
	vTexCoord0 = gl_Vertex.xy;
	float x = gl_Vertex.x;
	float y = gl_Vertex.y;
	float smin = 0; float smax = 1;
	float tmin = 0; float tmax = 1;
	float s = smin + x*(smax-smin);float t = tmin + y*(tmax-tmin);float u = s;float v = t;
	
	float newX = (2.0/3)*(cos(u)*cos(2*v)+sqrt(2)*sin(u)*cos(v))*cos(u) /
			     (sqrt(2) - sin(2*u)*sin(3*v));
	float newZ = (2.0/3)*(cos(u)*sin(2*v)-sqrt(2)*sin(u)*sin(v))*cos(u) /
			     (sqrt(2)-sin(2*u)*sin(3*v));
	float newY = sqrt(2)*pow(cos(u),2) / (sqrt(2) - sin(2*u)*sin(2*v));
	// ---------------------------
	vec4 vertex = uModelMatrix * vec4(newX, newY, newZ, 1.0);
	vPosition = vertex.xyz;
	gl_Position = (uProjectionMatrix * (uViewMatrix * vertex));
}

