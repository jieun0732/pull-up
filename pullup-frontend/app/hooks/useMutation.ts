import { useState } from "react";
import { API } from "@/lib/API";

type HttpMethod = "POST" | "PUT" | "DELETE";

interface UsePostResponse<T> {
  myMuate: (body: T) => Promise<void>;
  data: any;
  error: any;
  loading: boolean;
}

const useMutation = <T>(
  url: string,
  method: HttpMethod = "POST",
): UsePostResponse<T> => {
  const [data, setData] = useState<any>(null);
  const [error, setError] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const myMuate = async (body: T) => {
    setLoading(true);
    setError(null); // Reset error state before new request
    try {
      const response = await fetch(`${API}/${url}`, {
        method,
        // headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const json = await response.json();
      setData(json);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  return { myMuate, data, error, loading };
};

export default useMutation;
