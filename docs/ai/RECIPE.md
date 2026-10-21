# MCC External Service Example

## Overview

Integrate the Music Content Center external-service demo with RTC, music preload/playback and karaoke scoring.

Repository: [AgoraIO-Community/Agora-MccExService-Example](https://github.com/AgoraIO-Community/Agora-MccExService-Example)

## Prerequisites and Setup

1. Obtain compatible Agora MCC external-service and music-provider access, including test music entitlement.
2. For Android, open `Android/Agora-MccExService-Example` and set `APP_ID`, `APP_CERTIFICATE`, `YSD_APP_ID`, `YSD_APP_KEY`, `YSD_APP_TOKEN` and `YSD_APP_UID` in its `local.properties`. Sync and run.
3. For iOS, work in `iOS/AgoraMccExServiceExample/`, run `pod install`, configure the demo's `Config.swift` and open `AgoraMccExServiceExample.xcworkspace`. Use the repository's actual checkout rather than the placeholder clone URL in its README.
4. Initialize the sample's RTC and music service flow, preload a permitted song, then play and score.

## What To Check

- Confirm service initialization and music preload callbacks succeed.
- Verify playback and pitch/scoring data update together.
- Stop playback and leave the session; verify music and RTC resources release.

## Integration Notes

Music-provider credentials, rights and service SDK availability are prerequisites. Use a backend for privileged production credentials rather than shipping test secrets in a client.

Keep App Certificates, customer secrets and privileged token generation on a trusted backend for production. Use only your own test credentials while configuring historical demos; do not commit them. Preserve the sample's existing dependency versions and lifecycle until a separate migration is tested.

## Source References

- [Android/Agora-MccExService-Example/README.md](../../Android/Agora-MccExService-Example/README.md)
- [iOS/AgoraMccExServiceExample/README.md](../../iOS/AgoraMccExServiceExample/README.md)

## Validation Status

This recipe was checked against the repository's source and setup documents. The platform build and live service/device flow have not been run as part of this documentation change. Complete the checks above in an environment with the required SDKs, hardware and service access before treating it as a validated deployment.

## Implementation Prompt

```text
You are working in AgoraIO-Community/Agora-MccExService-Example. Use docs/ai/RECIPE.md and its linked source files to adapt the MCC External Service Example reference to this project.

Start by inspecting the actual sample directory and configuration described above. Resolve its listed missing prerequisites before claiming it is runnable. Preserve the existing architecture and compatible dependency versions. Implement the recipe's feature-specific flow, permissions and resource cleanup. Use a trusted backend for privileged credentials and tokens in production.

Run the repository-specific build or launch flow, then perform each What To Check item. Report the commands run, the observed results and anything blocked by service access, hardware or incomplete sample code. Do not claim live validation from a documentation or catalog build alone.
```
