# Host the predictor

The predictor is packaged as a Docker web service. It includes `weights.csv` and the data files the Java network needs, so it can make predictions without your computer running.

## Deploy with Render

1. Push this `basketball-stats` folder to a private GitHub repository. The data and weights are part of the current Docker image, so keep the repository private if you do not want to share them.
2. Create a new Render Blueprint from that repository. Render detects `render.yaml` and creates the `basketball-predictor` Docker web service.
3. After the deployment is live, open `https://YOUR-SERVICE.onrender.com/health`. It should return `{"status":"ok"}`.
4. Paste `https://YOUR-SERVICE.onrender.com` into the app's **Predictor server URL** field.

The app already accepts a standard HTTPS base URL. No tunnel is needed after deployment.

## Local Docker check

If Docker Desktop is installed, run this from the project folder:

```powershell
docker build -t basketball-predictor .
docker run --rm -p 8080:8080 basketball-predictor
```

Then visit `http://localhost:8080/health`.
