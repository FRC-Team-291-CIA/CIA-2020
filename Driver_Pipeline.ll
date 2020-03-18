//
// Inputs
//
Inputs
{
	Mat source0;
}

//
// Variables
//
Outputs
{
	Mat normalizeOutput;
}

//
// Steps
//

Step Normalize0
{
    Mat normalizeInput = source0;
    NormalizeType normalizeType = NORM_MINMAX;
    Double normalizeAlpha = 0.0;
    Double normalizeBeta = 255;

    normalize(normalizeInput, normalizeType, normalizeAlpha, normalizeBeta, normalizeOutput);
}




