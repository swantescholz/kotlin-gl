void main (void)  {
	vec4 pfl = calcPerFragmentLighting();
	vec4 color = pfl;
	color = clamp(color, vec4(0,0,0,0), vec4(1,1,1,1));
	color = vec4(color.rgb, uMaterial.ambient.a);
	gl_FragColor = color;
}
