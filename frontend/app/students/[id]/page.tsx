"use client";

import { useEffect, useMemo, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Link from "next/link";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";

type StatusType = "仮申込" | "本申込" | "受講中" | "受講終了";
const STATUS_OPTIONS: StatusType[] = ["仮申込", "本申込", "受講中", "受講終了"];

function getStatusColor(status: StatusType) {
  switch (status) {
    case "仮申込":
      return "bg-muted text-muted-foreground";
    case "本申込":
      return "bg-chart-2/15 text-chart-2";
    case "受講中":
      return "bg-chart-1/15 text-chart-1";
    case "受講終了":
      return "bg-secondary text-secondary-foreground";
    default:
      return "bg-muted text-muted-foreground";
  }
}

function formatDate(value: any): string {
  if (!value) return "-";
  if (typeof value === "string" && value.length >= 10) return value.slice(0, 10);
  return String(value);
}

export default function Page() {
  const params = useParams<{ id: string }>();
  const id = params?.id;
  const router = useRouter();

  const apiBase = useMemo(() => {
    return process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";
  }, []);

  const [data, setData] = useState<any>(null);
  const [error, setError] = useState<string | null>(null);

  // ステータス変更UI用
  const [statusDraft, setStatusDraft] = useState<Record<string, StatusType>>({});
  const [statusSaving, setStatusSaving] = useState<Record<string, boolean>>({});
  const [statusError, setStatusError] = useState<string | null>(null);

  const refetch = async () => {
    if (!id) return;
    const res = await fetch(`/api/student/${id}`, { cache: "no-store" });
    if (!res.ok) throw new Error(`API error: ${res.status} ${res.statusText}`);
    const json = await res.json();
    setData(json);
  };

  useEffect(() => {
    if (!id) return;

    (async () => {
      try {
        setError(null);
        setData(null);
        await refetch();
      } catch (e: any) {
        setError(e?.message ?? "unknown error");
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id, apiBase]);

  const student = data?.student;
  const courseList: any[] = Array.isArray(data?.studentCourseList) ? data.studentCourseList : [];

  const primaryCourse = useMemo(() => {
    if (courseList.length === 0) return null;
    return courseList.find((c) => c?.status === "受講中") ?? courseList[courseList.length - 1];
  }, [courseList]);

  const updateCourseStatus = async (studentCourseId: number, status: StatusType) => {
    const res = await fetch(`${apiBase}/updateCourseStatus`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ studentCourseId, status }),
    });

    if (!res.ok) {
      const text = await res.text().catch(() => "");
      throw new Error(text || `ステータス更新に失敗しました (HTTP ${res.status})`);
    }
  };

  return (
    <div className="min-h-screen bg-muted/30">
      <div className="container mx-auto py-8 px-4 md:px-8">
        <div className="mb-6 flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold tracking-tight text-foreground">学生詳細</h1>
            <p className="mt-1 text-sm text-muted-foreground">id: {id ?? "-"}</p>
          </div>

          <div className="flex gap-2">
            <Link href={`/students/${id}/edit`}>
              <Button disabled={!id}>編集</Button>
            </Link>
            <Button variant="outline" onClick={() => router.push("/students")}>
              一覧へ戻る
            </Button>
          </div>
        </div>

        {error && (
          <Card className="p-4 border-destructive/50">
            <p className="text-destructive font-semibold">取得に失敗しました</p>
            <pre className="mt-2 text-sm whitespace-pre-wrap">{error}</pre>
          </Card>
        )}

        {!error && !data && (
          <Card className="p-6">
            <p className="text-muted-foreground">Loading...</p>
          </Card>
        )}

        {data && (
          <div className="grid gap-6">
            {/* 基本情報 */}
            <Card className="p-6">
              <div className="mb-4 flex items-center justify-between">
                <h2 className="text-xl font-semibold">基本情報</h2>
                {primaryCourse?.status && (
                  <Badge variant="secondary" className={getStatusColor(primaryCourse.status as StatusType)}>
                    {primaryCourse.status}
                  </Badge>
                )}
              </div>

              <div className="grid gap-3 md:grid-cols-2">
                <div>
                  <div className="text-sm text-muted-foreground">氏名</div>
                  <div className="font-medium">{student?.name ?? "-"}</div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">ふりがな</div>
                  <div className="font-medium">{student?.furigana ?? "-"}</div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">メール</div>
                  <div className="font-medium">{student?.email ?? "-"}</div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">居住地</div>
                  <div className="font-medium">{student?.city ?? "-"}</div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">年齢</div>
                  <div className="font-medium">{student?.age ?? "-"}</div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">性別</div>
                  <div className="font-medium">{student?.gender ?? "-"}</div>
                </div>
                <div className="md:col-span-2">
                  <div className="text-sm text-muted-foreground">備考</div>
                  <div className="font-medium whitespace-pre-wrap">{student?.remark ?? "-"}</div>
                </div>
              </div>
            </Card>

            {/* 受講コース一覧 */}
            <Card className="p-6">
              <div className="mb-4 flex items-center justify-between">
                <h2 className="text-xl font-semibold">受講コース</h2>
              </div>

              {statusError && (
                <div className="mb-3 text-sm text-destructive whitespace-pre-wrap">
                  {statusError}
                </div>
              )}

              {courseList.length === 0 ? (
                <p className="text-muted-foreground">コース情報がありません</p>
              ) : (
                <div className="grid gap-3">
                  {courseList.map((c) => {
                    const courseId = Number(c?.id); // ←バックエンドの studentCourseId に相当（想定）
                    const currentStatus = (c?.status ?? "仮申込") as StatusType;
                    const key = String(courseId);

                    const draft = statusDraft[key] ?? currentStatus;
                    const saving = statusSaving[key] ?? false;

                    return (
                      <div
                        key={String(c?.id ?? Math.random())}
                        className="rounded-lg border bg-background p-4 flex flex-col md:flex-row md:items-center md:justify-between gap-3"
                      >
                        <div>
                          <div className="font-medium">{c?.courseName ?? "-"}</div>
                          <div className="mt-1 text-sm text-muted-foreground">
                            受講開始: {formatDate(c?.enrollmentDate)} / 修了: {formatDate(c?.completionDate)}
                          </div>
                        </div>

                        <div className="flex items-center gap-2">
                          <select
                            className="h-9 rounded-md border bg-background px-3 text-sm"
                            value={draft}
                            onChange={(e) =>
                              setStatusDraft((prev) => ({
                                ...prev,
                                [key]: e.target.value as StatusType,
                              }))
                            }
                            disabled={!Number.isFinite(courseId) || saving}
                          >
                            {STATUS_OPTIONS.map((s) => (
                              <option key={s} value={s}>
                                {s}
                              </option>
                            ))}
                          </select>

                          <Button
                            size="sm"
                            disabled={
                              saving ||
                              !Number.isFinite(courseId) ||
                              draft === currentStatus
                            }
                            onClick={async () => {
                              try {
                                setStatusError(null);
                                setStatusSaving((prev) => ({ ...prev, [key]: true }));

                                await updateCourseStatus(courseId, draft);

                                // 更新後は再取得して表示を最新化
                                await refetch();
                              } catch (e: any) {
                                setStatusError(e?.message ?? "ステータス更新に失敗しました");
                              } finally {
                                setStatusSaving((prev) => ({ ...prev, [key]: false }));
                              }
                            }}
                          >
                            {saving ? "更新中…" : "更新"}
                          </Button>

                          <Badge
                            variant="secondary"
                            className={getStatusColor(currentStatus)}
                          >
                            {currentStatus}
                          </Badge>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </Card>
          </div>
        )}
      </div>
    </div>
  );
}
