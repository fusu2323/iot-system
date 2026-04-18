# Phase 9: 场景设备联动 - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-18
**Phase:** 09-scene-device
**Areas discussed:** trigger vs toggle, device target state, disable behavior

---

## Gray Area 1: trigger() vs toggle()

| Option | Description | Selected |
|--------|-------------|----------|
| A) toggle() only | Sync happens when scene is enabled/disabled via toggle button | |
| B) trigger() only | Sync happens only when trigger() is explicitly called | ✓ |
| C) Both | toggle() enables scene + syncs; trigger() re-syncs | |
| D) trigger() replaces toggle() | Remove toggle(), trigger() becomes the enable action | |

**User's choice:** "claude decide"
**Notes:** Recommendation was B — requirement says "触发场景时" (trigger), and toggle() handles mutex which should stay separate. trigger() is the explicit execution action.

---

## Gray Area 2: Device target state

| Option | Description | Selected |
|--------|-------------|----------|
| A) Always 1 (enabled) | Simplest, all devices go to enabled status | ✓ |
| B) Read from SceneDevice.config | Config JSON stores per-device target state | |
| C) Always 1 + log | All enabled, with log showing which devices were affected | |

**User's choice:** "claude decide"
**Notes:** Recommendation was A — no per-device config needed for SCENE-03 scope. Keep it simple.

---

## Gray Area 3: Disable behavior

| Option | Description | Selected |
|--------|-------------|----------|
| A) All set to 0 | Clean slate, devices explicitly disabled | ✓ |
| B) All set to 0 only on trigger() | trigger handles full lifecycle | |
| C) Leave unchanged | Don't touch device states on disable | |

**User's choice:** "claude decide"
**Notes:** Recommendation was A — symmetric lifecycle, predictable. "同步更新" implies clean in/out.

---

## Claude's Discretion

All three gray areas deferred to Claude's judgment based on:
- Requirement wording ("触发场景时") pointing to trigger()
- Simplicity principle — no per-device config parsing for SCENE-03
- Symmetric lifecycle for predictability

## Deferred Ideas

- **Config per-device target state** — SceneDevice.config could store per-device target status in future (Phase 12+)
