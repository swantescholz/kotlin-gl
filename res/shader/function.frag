
void main (void)  {
	float a = vPosition.x;
	float b = vPosition.y;
	float c = vPosition.z;
	float x = vTexCoord0.x;
	float y = vTexCoord0.y;
	float red   = x;
	float green = 0.0;
	float blue  = y;
	float alpha = 1.0;
	vec4 color = vec4(red,green,blue,alpha);
	color = clamp(color, vec4(0,0,0,0), vec4(1,1,1,1));
	gl_FragColor = color;
}